//
// EvhUserIdentifierDTO.h
// generated at 2016-04-22 13:56:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserIdentifierDTO
//
@interface EvhUserIdentifierDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* identifierType;

@property(nonatomic, copy) NSString* identifierToken;

@property(nonatomic, copy) NSNumber* claimStatus;

@property(nonatomic, copy) NSString* verifyCode;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

