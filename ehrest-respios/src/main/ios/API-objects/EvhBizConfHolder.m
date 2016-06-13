//
// EvhBizConfHolder.m
//
#import "EvhBizConfHolder.h"
#import "EvhObject.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBizConfHolder
//

@implementation EvhBizConfHolder

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBizConfHolder* obj = [EvhBizConfHolder new];
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
    if(self.data)
        [jsonObject setObject: self.data forKey: @"data"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.data = [jsonObject objectForKey: @"data"];
        if(self.data && [self.data isEqual:[NSNull null]])
            self.data = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
