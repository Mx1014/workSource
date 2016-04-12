//
// EvhPlateNumberCommand.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPlateNumberCommand
//
@interface EvhPlateNumberCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* companyName;

@property(nonatomic, copy) NSString* phoneNumber;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSNumber* userId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

