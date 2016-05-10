//
// EvhSyncInsAppsCommand.m
//
#import "EvhSyncInsAppsCommand.h"
#import "EvhAppInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncInsAppsCommand
//

@implementation EvhSyncInsAppsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSyncInsAppsCommand* obj = [EvhSyncInsAppsCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _appInfos = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.appInfos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAppInfo* item in self.appInfos) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"appInfos"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"appInfos"];
            for(id itemJson in jsonArray) {
                EvhAppInfo* item = [EvhAppInfo new];
                
                [item fromJson: itemJson];
                [self.appInfos addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
