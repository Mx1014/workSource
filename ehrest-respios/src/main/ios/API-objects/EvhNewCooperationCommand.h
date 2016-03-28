//
// EvhNewCooperationCommand.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewCooperationCommand
//
@interface EvhNewCooperationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* cooperationType;

@property(nonatomic, copy) NSString* provinceName;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSString* communityNames;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* applicantName;

@property(nonatomic, copy) NSString* applicantOccupation;

@property(nonatomic, copy) NSString* applicantPhone;

@property(nonatomic, copy) NSString* applicantEmail;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

