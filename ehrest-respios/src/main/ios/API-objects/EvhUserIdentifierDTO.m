//
// EvhUserIdentifierDTO.m
//
#import "EvhUserIdentifierDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserIdentifierDTO
//

@implementation EvhUserIdentifierDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserIdentifierDTO* obj = [EvhUserIdentifierDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.identifierType)
        [jsonObject setObject: self.identifierType forKey: @"identifierType"];
    if(self.identifierToken)
        [jsonObject setObject: self.identifierToken forKey: @"identifierToken"];
    if(self.claimStatus)
        [jsonObject setObject: self.claimStatus forKey: @"claimStatus"];
    if(self.verifyCode)
        [jsonObject setObject: self.verifyCode forKey: @"verifyCode"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.identifierType = [jsonObject objectForKey: @"identifierType"];
        if(self.identifierType && [self.identifierType isEqual:[NSNull null]])
            self.identifierType = nil;

        self.identifierToken = [jsonObject objectForKey: @"identifierToken"];
        if(self.identifierToken && [self.identifierToken isEqual:[NSNull null]])
            self.identifierToken = nil;

        self.claimStatus = [jsonObject objectForKey: @"claimStatus"];
        if(self.claimStatus && [self.claimStatus isEqual:[NSNull null]])
            self.claimStatus = nil;

        self.verifyCode = [jsonObject objectForKey: @"verifyCode"];
        if(self.verifyCode && [self.verifyCode isEqual:[NSNull null]])
            self.verifyCode = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
