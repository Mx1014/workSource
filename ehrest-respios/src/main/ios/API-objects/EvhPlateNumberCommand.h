//
// EvhPlateNumberCommand.h
// generated at 2016-03-25 17:08:11 
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

