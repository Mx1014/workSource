//
// EvhGetCardPaidQrCodeDTO.m
//
#import "EvhGetCardPaidQrCodeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCardPaidQrCodeDTO
//

@implementation EvhGetCardPaidQrCodeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetCardPaidQrCodeDTO* obj = [EvhGetCardPaidQrCodeDTO new];
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
    if(self.code)
        [jsonObject setObject: self.code forKey: @"code"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.code = [jsonObject objectForKey: @"code"];
        if(self.code && [self.code isEqual:[NSNull null]])
            self.code = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
