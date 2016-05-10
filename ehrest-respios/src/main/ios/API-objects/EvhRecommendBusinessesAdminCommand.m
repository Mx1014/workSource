//
// EvhRecommendBusinessesAdminCommand.m
//
#import "EvhRecommendBusinessesAdminCommand.h"
#import "EvhBusinessScope.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendBusinessesAdminCommand
//

@implementation EvhRecommendBusinessesAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRecommendBusinessesAdminCommand* obj = [EvhRecommendBusinessesAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _scopes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.recommendStatus)
        [jsonObject setObject: self.recommendStatus forKey: @"recommendStatus"];
    if(self.scopes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBusinessScope* item in self.scopes) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"scopes"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.recommendStatus = [jsonObject objectForKey: @"recommendStatus"];
        if(self.recommendStatus && [self.recommendStatus isEqual:[NSNull null]])
            self.recommendStatus = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"scopes"];
            for(id itemJson in jsonArray) {
                EvhBusinessScope* item = [EvhBusinessScope new];
                
                [item fromJson: itemJson];
                [self.scopes addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
