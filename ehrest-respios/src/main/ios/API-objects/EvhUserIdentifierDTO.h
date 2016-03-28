//
// EvhUserIdentifierDTO.h
// generated at 2016-03-28 15:56:07 
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

