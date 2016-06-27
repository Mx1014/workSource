//
// EvhBizOrderHolder.m
//
#import "EvhBizOrderHolder.h"
#import "EvhObject.h"
#import "EvhBoolean.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBizOrderHolder
//

@implementation EvhBizOrderHolder

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBizOrderHolder* obj = [EvhBizOrderHolder new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.body)
        [jsonObject setObject: self.body forKey: @"body"];
    if(self.result) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.result toJson: dic];
        
        [jsonObject setObject: dic forKey: @"result"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.body = [jsonObject objectForKey: @"body"];
        if(self.body && [self.body isEqual:[NSNull null]])
            self.body = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"result"];

        self.result = [EvhBoolean new];
        self.result = [self.result fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
