//
// EvhEncriptInfoDTO.m
//
#import "EvhEncriptInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEncriptInfoDTO
//

@implementation EvhEncriptInfoDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEncriptInfoDTO* obj = [EvhEncriptInfoDTO new];
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
    if(self.plainText)
        [jsonObject setObject: self.plainText forKey: @"plainText"];
    if(self.plainTextHash)
        [jsonObject setObject: self.plainTextHash forKey: @"plainTextHash"];
    if(self.salt)
        [jsonObject setObject: self.salt forKey: @"salt"];
    if(self.encryptText)
        [jsonObject setObject: self.encryptText forKey: @"encryptText"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.plainText = [jsonObject objectForKey: @"plainText"];
        if(self.plainText && [self.plainText isEqual:[NSNull null]])
            self.plainText = nil;

        self.plainTextHash = [jsonObject objectForKey: @"plainTextHash"];
        if(self.plainTextHash && [self.plainTextHash isEqual:[NSNull null]])
            self.plainTextHash = nil;

        self.salt = [jsonObject objectForKey: @"salt"];
        if(self.salt && [self.salt isEqual:[NSNull null]])
            self.salt = nil;

        self.encryptText = [jsonObject objectForKey: @"encryptText"];
        if(self.encryptText && [self.encryptText isEqual:[NSNull null]])
            self.encryptText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
