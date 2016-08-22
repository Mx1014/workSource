//
// EvhPayZuolinRefundResponse.m
//
#import "EvhPayZuolinRefundResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayZuolinRefundResponse
//

@implementation EvhPayZuolinRefundResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPayZuolinRefundResponse* obj = [EvhPayZuolinRefundResponse new];
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
    if(self.version)
        [jsonObject setObject: self.version forKey: @"version"];
    if(self.errorCode)
        [jsonObject setObject: self.errorCode forKey: @"errorCode"];
    if(self.errorScope)
        [jsonObject setObject: self.errorScope forKey: @"errorScope"];
    if(self.errorDescription)
        [jsonObject setObject: self.errorDescription forKey: @"errorDescription"];
    if(self.errorDetails)
        [jsonObject setObject: self.errorDetails forKey: @"errorDetails"];
    if(self.response)
        [jsonObject setObject: self.response forKey: @"response"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.version = [jsonObject objectForKey: @"version"];
        if(self.version && [self.version isEqual:[NSNull null]])
            self.version = nil;

        self.errorCode = [jsonObject objectForKey: @"errorCode"];
        if(self.errorCode && [self.errorCode isEqual:[NSNull null]])
            self.errorCode = nil;

        self.errorScope = [jsonObject objectForKey: @"errorScope"];
        if(self.errorScope && [self.errorScope isEqual:[NSNull null]])
            self.errorScope = nil;

        self.errorDescription = [jsonObject objectForKey: @"errorDescription"];
        if(self.errorDescription && [self.errorDescription isEqual:[NSNull null]])
            self.errorDescription = nil;

        self.errorDetails = [jsonObject objectForKey: @"errorDetails"];
        if(self.errorDetails && [self.errorDetails isEqual:[NSNull null]])
            self.errorDetails = nil;

        self.response = [jsonObject objectForKey: @"response"];
        if(self.response && [self.response isEqual:[NSNull null]])
            self.response = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
