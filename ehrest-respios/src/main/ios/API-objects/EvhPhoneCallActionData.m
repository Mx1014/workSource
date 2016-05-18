//
// EvhPhoneCallActionData.m
//
#import "EvhPhoneCallActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPhoneCallActionData
//

@implementation EvhPhoneCallActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPhoneCallActionData* obj = [EvhPhoneCallActionData new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _callPhones = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.callPhones) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.callPhones) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"callPhones"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"callPhones"];
            for(id itemJson in jsonArray) {
                [self.callPhones addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
