//
// EvhNewCooperationCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
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

