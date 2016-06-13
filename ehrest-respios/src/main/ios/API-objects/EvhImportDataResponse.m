//
// EvhImportDataResponse.m
//
#import "EvhImportDataResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImportDataResponse
//

@implementation EvhImportDataResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhImportDataResponse* obj = [EvhImportDataResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _logs = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.totalCount)
        [jsonObject setObject: self.totalCount forKey: @"totalCount"];
    if(self.failCount)
        [jsonObject setObject: self.failCount forKey: @"failCount"];
    if(self.logs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.logs) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"logs"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.totalCount = [jsonObject objectForKey: @"totalCount"];
        if(self.totalCount && [self.totalCount isEqual:[NSNull null]])
            self.totalCount = nil;

        self.failCount = [jsonObject objectForKey: @"failCount"];
        if(self.failCount && [self.failCount isEqual:[NSNull null]])
            self.failCount = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"logs"];
            for(id itemJson in jsonArray) {
                [self.logs addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
