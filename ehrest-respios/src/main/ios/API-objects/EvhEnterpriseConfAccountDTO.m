//
// EvhEnterpriseConfAccountDTO.m
//
#import "EvhEnterpriseConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseConfAccountDTO
//

@implementation EvhEnterpriseConfAccountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseConfAccountDTO* obj = [EvhEnterpriseConfAccountDTO new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.enterpriseDisplayName)
        [jsonObject setObject: self.enterpriseDisplayName forKey: @"enterpriseDisplayName"];
    if(self.enterpriseContactor)
        [jsonObject setObject: self.enterpriseContactor forKey: @"enterpriseContactor"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.useStatus)
        [jsonObject setObject: self.useStatus forKey: @"useStatus"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.totalAccount)
        [jsonObject setObject: self.totalAccount forKey: @"totalAccount"];
    if(self.validAccount)
        [jsonObject setObject: self.validAccount forKey: @"validAccount"];
    if(self.buyChannel)
        [jsonObject setObject: self.buyChannel forKey: @"buyChannel"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.enterpriseDisplayName = [jsonObject objectForKey: @"enterpriseDisplayName"];
        if(self.enterpriseDisplayName && [self.enterpriseDisplayName isEqual:[NSNull null]])
            self.enterpriseDisplayName = nil;

        self.enterpriseContactor = [jsonObject objectForKey: @"enterpriseContactor"];
        if(self.enterpriseContactor && [self.enterpriseContactor isEqual:[NSNull null]])
            self.enterpriseContactor = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.useStatus = [jsonObject objectForKey: @"useStatus"];
        if(self.useStatus && [self.useStatus isEqual:[NSNull null]])
            self.useStatus = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.totalAccount = [jsonObject objectForKey: @"totalAccount"];
        if(self.totalAccount && [self.totalAccount isEqual:[NSNull null]])
            self.totalAccount = nil;

        self.validAccount = [jsonObject objectForKey: @"validAccount"];
        if(self.validAccount && [self.validAccount isEqual:[NSNull null]])
            self.validAccount = nil;

        self.buyChannel = [jsonObject objectForKey: @"buyChannel"];
        if(self.buyChannel && [self.buyChannel isEqual:[NSNull null]])
            self.buyChannel = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
