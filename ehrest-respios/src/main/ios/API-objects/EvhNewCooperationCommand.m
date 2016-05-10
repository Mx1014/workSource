//
// EvhNewCooperationCommand.m
//
#import "EvhNewCooperationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewCooperationCommand
//

@implementation EvhNewCooperationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNewCooperationCommand* obj = [EvhNewCooperationCommand new];
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
    if(self.cooperationType)
        [jsonObject setObject: self.cooperationType forKey: @"cooperationType"];
    if(self.provinceName)
        [jsonObject setObject: self.provinceName forKey: @"provinceName"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.areaName)
        [jsonObject setObject: self.areaName forKey: @"areaName"];
    if(self.communityNames)
        [jsonObject setObject: self.communityNames forKey: @"communityNames"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.contactType)
        [jsonObject setObject: self.contactType forKey: @"contactType"];
    if(self.contactToken)
        [jsonObject setObject: self.contactToken forKey: @"contactToken"];
    if(self.applicantName)
        [jsonObject setObject: self.applicantName forKey: @"applicantName"];
    if(self.applicantOccupation)
        [jsonObject setObject: self.applicantOccupation forKey: @"applicantOccupation"];
    if(self.applicantPhone)
        [jsonObject setObject: self.applicantPhone forKey: @"applicantPhone"];
    if(self.applicantEmail)
        [jsonObject setObject: self.applicantEmail forKey: @"applicantEmail"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.cooperationType = [jsonObject objectForKey: @"cooperationType"];
        if(self.cooperationType && [self.cooperationType isEqual:[NSNull null]])
            self.cooperationType = nil;

        self.provinceName = [jsonObject objectForKey: @"provinceName"];
        if(self.provinceName && [self.provinceName isEqual:[NSNull null]])
            self.provinceName = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.areaName = [jsonObject objectForKey: @"areaName"];
        if(self.areaName && [self.areaName isEqual:[NSNull null]])
            self.areaName = nil;

        self.communityNames = [jsonObject objectForKey: @"communityNames"];
        if(self.communityNames && [self.communityNames isEqual:[NSNull null]])
            self.communityNames = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.contactType = [jsonObject objectForKey: @"contactType"];
        if(self.contactType && [self.contactType isEqual:[NSNull null]])
            self.contactType = nil;

        self.contactToken = [jsonObject objectForKey: @"contactToken"];
        if(self.contactToken && [self.contactToken isEqual:[NSNull null]])
            self.contactToken = nil;

        self.applicantName = [jsonObject objectForKey: @"applicantName"];
        if(self.applicantName && [self.applicantName isEqual:[NSNull null]])
            self.applicantName = nil;

        self.applicantOccupation = [jsonObject objectForKey: @"applicantOccupation"];
        if(self.applicantOccupation && [self.applicantOccupation isEqual:[NSNull null]])
            self.applicantOccupation = nil;

        self.applicantPhone = [jsonObject objectForKey: @"applicantPhone"];
        if(self.applicantPhone && [self.applicantPhone isEqual:[NSNull null]])
            self.applicantPhone = nil;

        self.applicantEmail = [jsonObject objectForKey: @"applicantEmail"];
        if(self.applicantEmail && [self.applicantEmail isEqual:[NSNull null]])
            self.applicantEmail = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
